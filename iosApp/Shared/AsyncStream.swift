import shared

struct FlowThrowingStream<T>: AsyncSequence {
    func makeAsyncIterator() -> FlowAsyncThrowingIterator {
        FlowAsyncThrowingIterator(FlowsKt.asAsyncIterable(flow, context: context))
    }

    typealias AsyncIterator = FlowAsyncThrowingIterator

    typealias Element = T

    private let flow: Flow
    private let context: KotlinCoroutineContext

    init (_ type: T.Type, flow: Flow, context: KotlinCoroutineContext) {
        self.flow = flow
        self.context = context
    }

    struct FlowAsyncThrowingIterator: AsyncIteratorProtocol {
        private let iterator: IteratorAsync

        init(_ iterator: IteratorAsync) {
            self.iterator = iterator
        }

        @MainActor
        func next() async throws -> T? {
            if(Task.isCancelled) {
                iterator.cancel()
                return nil
            }
            return try await iterator.next() as? T? ?? nil
        }

        typealias Element = T
    }
}

struct FlowStream<T>: AsyncSequence {
    func makeAsyncIterator() -> FlowAsyncIterator {
        FlowAsyncIterator(FlowsKt.asAsyncIterable(flow, context: context))
    }

    typealias AsyncIterator = FlowAsyncIterator

    typealias Element = T

    private let flow: Flow
    private let context: KotlinCoroutineContext

    init (_ type: T.Type, flow: Flow, context: KotlinCoroutineContext) {
        self.flow = flow
        self.context = context
    }

    struct FlowAsyncIterator: AsyncIteratorProtocol {
        private let iterator: IteratorAsync

        init(_ iterator: IteratorAsync) {
            self.iterator = iterator
        }

        @MainActor
        func next() async -> T? {
            if(Task.isCancelled) {
                iterator.cancel()
                return nil
            }
            return try! await iterator.next() as? T? ?? nil
        }

        typealias Element = T
    }
}

extension Flow {
    func streamThrowing<T>(_ t: T.Type, context: KotlinCoroutineContext) -> FlowThrowingStream<T> {
        FlowThrowingStream(t, flow: self, context: context)
    }
    func stream<T>(_ t: T.Type, context: KotlinCoroutineContext) -> FlowStream<T> {
        FlowStream(t, flow: self, context: context)
    }
}

extension Array {
    class Sequence: AsyncSequence, AsyncIteratorProtocol {
        typealias AsyncIterator = Sequence

        typealias Element = Array.Element

        private var index = -1
        private let array: Array<Element>

        init(_ array: Array<Element>) {
            self.array = array
        }

        func makeAsyncIterator() -> Sequence {
            self
        }

        func next() async -> Element? {
            guard index + 1 < array.count else {
                return nil
            }
            index += 1
            return array[index]
        }
    }

    var values: Sequence {
        Sequence(self)
    }
}

extension AsyncSequence {
    func collect() async rethrows -> [Element] {
        try await reduce(into: [Element]()) {
            $0.append($1)
        }
    }
}

class AsyncSuspendFunction0<R>: KotlinSuspendFunction0 {
    let function: () async throws -> R

    init(_ function: @escaping () async throws -> R) {
        self.function = function
    }

    @MainActor
    func invoke() async throws -> Any? {
        try await function()
    }
}

class AsyncSuspendFunction1<T, R>: KotlinSuspendFunction1 {
    let function: (T) async throws -> R

    init(_ function: @escaping (T) async throws -> R) {
        self.function = function
    }

    @MainActor
    func invoke(p1: Any?) async throws -> Any? {
        try await function(p1 as! T)
    }
}

class AsyncSuspendFunction2<T1, T2, R>: KotlinSuspendFunction2 {
    let function: (T1, T2) async throws -> R

    init(_ function: @escaping (T1, T2) async -> R) {
        self.function = function
    }

    @MainActor
    func invoke(p1: Any?, p2: Any?) async throws -> Any? {
        try await function(p1 as! T1, p2 as! T2)
    }
}
