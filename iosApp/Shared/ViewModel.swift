//
//  ViewModel.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 15.04.22.
//

import SwiftUI
import Combine
import clients

extension ViewModel: ObservableObject {
}

extension ObservableObject where Self: ViewModel {
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<String> {
        binding(flow: self[keyPath: keyPath], t: String.self)
    }
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<Int> {
        binding(flow: self[keyPath: keyPath], t: Int.self)
    }
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<Bool> {
        binding(flow: self[keyPath: keyPath], t: Bool.self)
    }
    
    func binding<T>(_ keyPath: KeyPath<Self, MutableStateFlow>, t: T.Type) -> Binding<T> {
        binding(flow: self[keyPath: keyPath], t: t)
    }

    func binding<T>(flow: MutableStateFlow, t: T.Type) -> Binding<T> {
        .init(get: {
            flow.value as! T
        }, set: {
            self.objectWillChange.send()
            flow.setValue($0)
        })
    }
}

extension IosContainer: ObservableObject { }
