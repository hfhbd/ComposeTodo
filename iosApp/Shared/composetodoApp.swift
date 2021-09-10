//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI
import shared
import Combine

@main
struct ComposeTodoApp: App {
    
    let container = IosContainer.init(scope: CoroutineScopeKt.MainScope(), protocol: URLProtocol.Companion().HTTP, host: "philips-macbook-air.local")
    
    var body: some Scene {
        WindowGroup {
            ContentView(container: container)
        }
    }
}

extension Binding {
    init(_ flow: MutableStateFlow) {
        self.init(get: {
            flow.value as! Value
        }) { newValue in
            flow.setValue(newValue)
        }
    }
}

private class Collector<T>: FlowCollector {
    let subject: PassthroughSubject<T, Error>
    init(subject: PassthroughSubject<T, Error>) {
        self.subject = subject
    }
    
    func emit(value: Any?) async throws -> KotlinUnit? {
        subject.send(value as! T)
        return KotlinUnit()
    }
}

extension Flow {
    func publisher<T>(_ type: T.Type) -> AnyPublisher<T, Error> {
        let subject = PassthroughSubject<T, Error>()
        let collector = Collector(subject: subject)
        
        self.collect(collector: collector) {  _, error in
            if let error = error {
                subject.send(completion: .failure(error))
            } else {
                subject.send(completion: .finished)
            }
        }
        return subject.eraseToAnyPublisher()
    }
}

extension Sequence where Element == AnyCancellable {
    func cancelAll() {
        forEach {
            $0.cancel()
        }
    }
}
