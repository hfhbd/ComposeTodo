import SwiftUI
import clients
import Combine

struct ContentView: View {
    @StateObject var container: IosContainer
    
    init () {
        let container = IosContainer()
        self._container = StateObject.init(wrappedValue: { container }())
        self.isLoggedIn = APILoggedOut(client: container.client)
    }

    @State private var isLoggedIn: API

    var body: some View {
        if let isLoggedIn = isLoggedIn as? APILoggedOut {
            TabView {
                NavigationView {
                    Login(viewModel: container.loginViewModel(api: isLoggedIn))
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
    init(viewModel: @autoclosure @escaping () -> LoginViewModel) {
        self._viewModel = StateObject(wrappedValue: viewModel())
    }

    @StateObject var viewModel: LoginViewModel

    @State private var error: Failure? = nil
    @State private var disableLogin = true

    var body: some View {
        Form {
            TextField("Username", text: viewModel.binding(\.userName))
            SecureField("Password", text: viewModel.binding(\.password))

            if let error = error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                viewModel.login()
            }
            .disabled(disableLogin)
        }.task {
            for await newError in viewModel.error.stream(Failure?.self) {
                self.error = newError
            }
        }.task {
            for await newEnabled in viewModel.enableLogin.stream(Bool.self) {
                self.disableLogin = !newEnabled
            }
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
