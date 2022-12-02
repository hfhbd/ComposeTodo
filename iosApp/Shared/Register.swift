//
//  Register.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 13.11.22.
//

import SwiftUI
import clients

struct Register: View {
    init(viewModel: @autoclosure @escaping () -> RegisterViewModel) {
        self._viewModel = StateObject(wrappedValue: viewModel())
    }
    
    @StateObject var viewModel: RegisterViewModel
    
    @State private var disableRegister = true
    @State private var error: Failure? = nil
    
    var body: some View {
        Form {
            TextField("Username", text: viewModel.binding(\.username))
            
            SecureField("Password", text: viewModel.binding(\.password))
            SecureField("Password Again", text: viewModel.binding(\.passwordAgain))
            
            TextField("First Name", text: viewModel.binding(\.firstName))
            TextField("Last Name", text: viewModel.binding(\.lastName))
        }.task {
            for await newError in viewModel.error.stream(Failure?.self) {
                self.error = newError
            }
        }.toolbar {
            Button("Register") {
                viewModel.register()
            }.disabled(disableRegister)
                .task {
                    for await newEnabled in viewModel.enableRegisterButton.stream(Bool.self) {
                        self.disableRegister = !newEnabled
                    }
                }
        }
    }
}

