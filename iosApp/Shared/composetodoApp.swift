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
    let container = IosContainer()
    
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


extension Flow {
    func publisher<T>(_ type: T.Type) -> AnyPublisher<T, Error> {
        let subject = PassthroughSubject<T, Error>()
        
        FlowsKt.collectOnMain(self, collector: { value in
            if let value = value as? T {
                subject.send(value)
            }
        }, completionHandler: { _, error in
            if let error = error {
                subject.send(completion: .failure(error))
            } else {
                subject.send(completion: .finished)
            }
        })
        return subject.eraseToAnyPublisher()
    }
}


extension Publisher {
    func toFlow(_ caller: String, canceling: @escaping (AnyCancellable) -> Void) -> Flow {
        FlowsKt.toFlow(caller: caller) { (onComplete, onReceive) in
            canceling(self.sink { it in
                switch it {
                case .failure(let error):
                    onComplete(error)
                case .finished:
                    onComplete(nil)
                }
            } receiveValue: { it in
                onReceive.invoke(p1: it) { it, error in
                    Swift.print("PublisherToFlowSuspendCompletionHandler with \(it) \(error)")
                }
            })
        }
    }
}

