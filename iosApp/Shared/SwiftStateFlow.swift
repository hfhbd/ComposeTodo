//
//  SwiftStateFlow.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 13.11.22.
//

import clients

class SwiftStateFlow: StateFlow, ObservableObject {
    let flow: StateFlow
    
    var value: Any? {
        flow.value
    }
    
    var replayCache: [Any] { flow.replayCache }
    
    func collect(collector: FlowCollector) async throws {
        try await flow.collect(collector: SwiftFlowCollector(collector: collector, objectWillChange: objectWillChange))
    }
    
    init(flow: StateFlow) {
        self.flow = flow
    }
    
    private class SwiftFlowCollector: FlowCollector {
        @MainActor
        func emit(value: Any?) async throws {
            objectWillChange.send()
            try await collector.emit(value: value)
        }
        
        let collector: FlowCollector
        let objectWillChange: ObjectWillChangePublisher
        
        init(collector: FlowCollector, objectWillChange: ObjectWillChangePublisher) {
            self.collector = collector
            self.objectWillChange = objectWillChange
        }
    }
}
