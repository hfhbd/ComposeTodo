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
