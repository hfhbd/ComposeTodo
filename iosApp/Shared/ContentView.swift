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
                    Login(viewModel: container.loginViewModel(api: isLoggedIn)) {
                        for await api in container.api.stream(API.self) {
                            self.isLoggedIn = api
                        }
                    }
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
    let action: () async -> Void

    @State private var username = ""
    @State private var password = ""
    @State private var error: Failure? = nil
    @State private var disableLogin = true

    var body: some View {
        Form {
            TextField("Username", text: $username).sync(flow: viewModel.userName, with: $username)
            SecureField("Password", text: $password).sync(flow: viewModel.password, with: $password)

            if let error = error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                viewModel.login()
            }
            .disabled(disableLogin)
        }.task {
            try! await viewModel.error.collect { (newError: Failure?) in
                self.error = newError
            }
        }.task {
            try! await viewModel.enableLogin.collect { (newEnabled: Bool) in
                self.disableLogin = !newEnabled
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

    @State private var disableRegister = true
    @State private var error: Failure? = nil

    var body: some View {
        Form {
            TextField("Username", text: $username).sync(flow: viewModel.username, with: $username)

            SecureField("Password", text: $password).sync(flow: viewModel.password, with: $password)
            SecureField("Password Again", text: $passwordAgain).sync(flow: viewModel.passwordAgain, with: $passwordAgain)

            TextField("First Name", text: $firstName).sync(flow: viewModel.firstName, with: $firstName)
            TextField("Last Name", text: $lastName).sync(flow: viewModel.lastName, with: $lastName)
        }.task {
            try! await viewModel.error.collect { (newError: Failure?) in
                self.error = newError
            }
        }.toolbar {
            Button("Register") {
                viewModel.register()
            }.disabled(disableRegister)
            .task {
                try! await viewModel.enableRegisterButton.collect { (newEnabled: Bool) in
                    self.disableRegister = !newEnabled
                }
            }
        }
    }
}

extension Todo: Swift.Identifiable { }

extension View {
    func sync<T: Equatable>(flow: MutableStateFlow, with: Binding<T>) -> some View {
        task {
            try! await flow.collect { (value: T) in
                with.wrappedValue = value
            }
        }.onChange(of: with.wrappedValue) { newValue in
            flow.setValue(newValue)
        }
    }
}
