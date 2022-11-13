//
//  Login.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 13.11.22.
//

import SwiftUI
import clients

struct Login: View {
    init(loginViewModel: @escaping () -> LoginViewModel) {
        self._loginViewModel = StateObject(wrappedValue: loginViewModel())
    }
    
    @StateObject private var loginViewModel: LoginViewModel
    
    var body: some View {
        LoginView(state: loginViewModel.state(coroutineScope: loginViewModel.lifecycleScope, clock: Molecule_runtimeRecompositionClock.immediate), updateUserName: {
            loginViewModel.updateUsername(new: $0)
        }, updatePassword: {
            loginViewModel.updatePassword(new: $0)
        }, login: {
            loginViewModel.login()
        })
    }
}

private struct LoginView: View {
    init(
        state: StateFlow,
        updateUserName: @escaping (String) -> Void,
        updatePassword: @escaping (String) -> Void,
        login: @escaping () -> Void
    ) {
        self.stateFlow = SwiftStateFlow(flow: state)
        self.updateUsername = updateUserName
        self.updatePassword = updatePassword
        self.login = login
    }
    let updateUsername: (String) -> Void
    let updatePassword: (String) -> Void
    let login: () -> Void
    
    @ObservedObject private var stateFlow: SwiftStateFlow
    
    var body: some View {
        let state = stateFlow.value as! LoginViewModel.LoginState
        
        return Form {
            TextField("Username", text: Binding(get: {
                state.username
            }, set: {
                updateUsername($0)
            }))
            SecureField("Password", text: Binding(get: {
                state.password
            }, set: {
                updatePassword($0)
            }))
            
            if let error = state.error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                login()
            }
            .disabled(!state.enableLogin)
        }.task {
            for await _ in stateFlow.stream(LoginViewModel.LoginState.self) { }
        }
    }
}
