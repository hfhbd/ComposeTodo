import Combine
import shared
import SwiftUI

struct FlowStream<T>: AsyncSequence {
    func makeAsyncIterator() -> FlowAsyncIterator {
        FlowAsyncIterator(flow: flow)
    }

    typealias AsyncIterator = FlowAsyncIterator

    typealias Element = T

    private let flow: Flow
    init (_ type: T.Type, flow: Flow) {
        self.flow = flow
    }

    struct FlowAsyncIterator: AsyncIteratorProtocol {
        private let iterator: IteratorAsync

        init(flow: Flow) {
            self.iterator = FlowsKt.asAsyncIterable(flow)
        }

        @MainActor
        func next() async -> T? {
            if(Task.isCancelled) {
                iterator.cancel()
                return nil
            }
            return try? await iterator.next() as! T?
        }

        typealias Element = T
    }
}

struct FlowStreamThrowing<T>: AsyncSequence {
    func makeAsyncIterator() -> FlowAsyncIterator {
        FlowAsyncIterator(flow: flow, onError: onError)
    }
    
    typealias AsyncIterator = FlowAsyncIterator
    
    typealias Element = T
    
    private let flow: Flow
    private let onError: T
    init (_ type: T.Type, flow: Flow, onError: T) {
        self.flow = flow
        self.onError = onError
    }
    
    struct FlowAsyncIterator: AsyncIteratorProtocol {
        private let iterator: IteratorAsync
        
        private let onError: T
        
        init(flow: Flow, onError: T) {
            self.iterator = FlowsKt.asAsyncIterable(flow)
            self.onError = onError
        }
        
        @MainActor
        func next() async throws -> T? {
            if(Task.isCancelled) {
                iterator.cancel()
                return nil
            }
            return (try? await iterator.next() as! T?) ?? onError
        }
        
        typealias Element = T
    }
}

extension Flow {
    func stream<T>(_ t: T.Type) -> FlowStream<T> {
        FlowStream(t, flow: self)
    }
    
    func streamThrowing<T>(_ t: T.Type, onError: T) -> FlowStreamThrowing<T> {
        FlowStreamThrowing(t, flow: self, onError: onError)
    }
}

extension AsyncSequence {
    func collect() async rethrows -> [Element] {
        try await reduce(into: [Element]()) {
            $0.append($1)
        }
    }
    
    func first() async rethrows -> Element {
        try await first(where: { _ in true })!
    }
}
