import SwiftUI
import shared

struct ContentView: View {
    let container: IosContainer

    init (container: IosContainer) {
        self.container = container
        self.isLoggedIn = API.LoggedOut(client: container.client)
    }

    @State private var isLoggedIn: API

    var body: some View {
        if let isLoggedIn = isLoggedIn as? API.LoggedOut {
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
        } else if let isLoggedIn = isLoggedIn as? API.LoggedIn {
            NavigationView {
                Todos(viewModel: container.todoViewModel(api: isLoggedIn))
                    .navigationTitle("Todos")
            }
        }
    }
}

struct Login: View {
    let viewModel: LoginViewModel

    @State private var username = ""
    @State private var password = ""
    @State private var error: Failure? = nil

    var body: some View {
        Form {
            TextField("Username", text: $username).task {
                for await value in self.viewModel.userName.stream(String.self) {
                    self.username = value
                }
            }.onChange(of: username) { newValue in
                self.viewModel.userName.setValue(newValue)
            }

            SecureField("Password", text: $password).task {
                for await value in self.viewModel.password.stream(String.self) {
                    self.password = value
                }
            }.onChange(of: password) { newValue in
                self.viewModel.password.setValue(newValue)
            }

            if let error = error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                viewModel.login()
            }
        }.task {
            for await newError in viewModel.error.stream(Failure?.self) {
                self.error = newError
            }
        }
    }
}

struct Register: View {
    let viewModel: RegisterViewModel

    @State private var username = ""
    @State private var password = ""
    @State private var passwordAgain = ""
    @State private var firstName = ""
    @State private var lastName = ""

    var body: some View {
        Form {
            TextField("Username", text: $username).task {
                for await value in self.viewModel.username.stream(String.self) {
                    self.username = value
                }
            }.onChange(of: username) { newValue in
                self.viewModel.username.setValue(newValue)
            }

            SecureField("Password", text: $password).task {
                for await value in self.viewModel.password.stream(String.self) {
                    self.password = value
                }
            }.onChange(of: password) { newValue in
                self.viewModel.password.setValue(newValue)
            }

            SecureField("Password Again", text: $passwordAgain).task {
                for await value in self.viewModel.passwordAgain.stream(String.self) {
                    self.passwordAgain = value
                }
            }.onChange(of: passwordAgain) { newValue in
                self.viewModel.passwordAgain.setValue(newValue)
            }

            TextField("First Name", text: $firstName).task {
                for await value in self.viewModel.firstName.stream(String.self) {
                    self.firstName = value
                }
            }.onChange(of: firstName) { newValue in
                self.viewModel.firstName.setValue(newValue)
            }

            TextField("Last Name", text: $lastName).task {
                for await value in self.viewModel.lastName.stream(String.self) {
                    self.lastName = value
                }
            }.onChange(of: lastName) { newValue in
                self.viewModel.lastName.setValue(newValue)
            }
        }.toolbar {
            Button("Register") {
                viewModel.register()
            }
        }
    }
}

extension Todo: Swift.Identifiable { }
