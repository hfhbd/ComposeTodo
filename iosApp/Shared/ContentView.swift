import SwiftUI
import clients
import Combine

struct ContentView: View {
    @ObservedObject var container: IosContainer
    
    init(container: IosContainer) {
        self._container = .init(initialValue: container)
        self.isLoggedIn = APILoggedOut(client: container.client)
    }

    @State private var isLoggedIn: API

    var body: some View {
        if let isLoggedIn = isLoggedIn as? APILoggedOut {
            TabView {
                NavigationView {
                    let loginViewModel = container.loginViewModel(api: isLoggedIn)
                    Login(state: {
                        loginViewModel
                    })
                        .navigationTitle("Login")
                }.tabItem {
                    Label("Login", systemImage: "person")
                }

                NavigationView {
                    Register(viewModel: container.registerViewModel(api: isLoggedIn))
                        .navigationTitle("Register")
                }.tabItem {
                    Label("Register", systemImage: "person.badge.plus")
                }
            }.task {
                for await api in container.api.stream(API.self) {
                    self.isLoggedIn = api
                }
            }
        } else if let isLoggedIn = isLoggedIn as? APILoggedIn {
            NavigationView {
                Todos(viewModel: container.todoViewModel(api: isLoggedIn))
                    .navigationTitle("Todos")
            }
        }
    }
}

struct Login: View {
    init(
        state: @escaping () -> StateFlow,
        updateUserName: @escaping (String) -> Void,
        updatePassword: @escaping (String) -> Void
    ) {
        self._stateFlow = StateObject(wrappedValue: {
            SwiftStateFlow(flow: state())
        }())
        self.updateUserName = updateUserName
        self.updatePassword = updatePassword
    }
    let updateUserName: (String) -> Void
    let updatePassword: (String) -> Void
    
    @StateObject private var stateFlow: SwiftStateFlow

    var body: some View {
        let state = stateFlow.value as! LoginViewModel.LoginState
        
        return Form {
            TextField("Username", text: Binding(get: {
                state.userName
            }, set: {
                updateUserName(new: $0)
            }))
            SecureField("Password", text: Binding(get: {
                state.userName
            }, set: {
                updatePassword(new: $0)
            }))
            
            if let error = state.error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                viewModel.login()
            }
            .disabled(!state.enableLogin)
        }.task {
            for await _ in stateFlow.stream(LoginViewModel.LoginState.self) { }
        }
    }
}

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

extension Todo: Swift.Identifiable { }

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
