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
    @MainActor
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<String> {
        binding(flow: self[keyPath: keyPath], t: String.self)
    }

    @MainActor
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<Int> {
        binding(flow: self[keyPath: keyPath], t: Int.self)
    }

    @MainActor
    func binding(_ keyPath: KeyPath<Self, MutableStateFlow>) -> Binding<Bool> {
        binding(flow: self[keyPath: keyPath], t: Bool.self)
    }

    @MainActor
    func binding<T>(_ keyPath: KeyPath<Self, MutableStateFlow>, t: T.Type) -> Binding<T> where T: Equatable {
        binding(flow: self[keyPath: keyPath], t: t)
    }

    @MainActor
    func binding<T>(flow: MutableStateFlow, t: T.Type) -> Binding<T> where T: Equatable {
        Task {
            let oldValue = flow.value as! T
            for await newValue in flow.stream(t) {
                if (oldValue != newValue) {
                    self.objectWillChange.send()
                    break
                }
            }
        }
        return .init(get: {
            flow.value as! T
        }, set: {
            flow.setValue($0)
        })
    }
}

extension IosContainer: ObservableObject { }
