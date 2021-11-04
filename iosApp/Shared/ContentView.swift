import SwiftUI
import CoreData
import shared

struct ContentView: View {
    let container: IosContainer

    init(container: IosContainer) {
        self.container = container
        self._isLoggedIn = .init(container.isLoggedIn)
    }
    
    @Binding private var isLoggedIn: API
    
    var body: some View {
        if (isLoggedIn is API.LoggedOut) {
            TabView {
                NavigationView {
                    Login(viewModel: container.loginViewModel(api: isLoggedIn as! API.LoggedOut))
                        .navigationTitle("Login")
                }.tabItem {
                    Label("Login", systemImage: "person")
                }
                NavigationView {
                    Register(viewModel: container.registerViewModel(api: isLoggedIn as! API.LoggedOut))
                        .navigationTitle("Register")
                }.tabItem {
                    Label("Register", systemImage: "person.badge.plus")
                }
            }
        } else {
            NavigationView {
                Todos(viewModel: container.todoViewModel(api: isLoggedIn as! API.LoggedIn))
                    .navigationTitle("Todos")
            }
        }
    }
}

struct Login: View {
    let viewModel: LoginViewModel
    
    init(viewModel: LoginViewModel) {
        self.viewModel = viewModel
        self._username = .init(viewModel.userName)
        self._password = .init(viewModel.password)
        self._error = .init(viewModel.error)
    }
    
    @Binding private var username: String
    @Binding private var password: String
    @Binding private var error: LoginViewModel.LoginResultFailure?

    var body: some View {
        Form {
            TextField("Username", text: $username)
            SecureField("Password", text: $password)

            if let error = error {
                Text(error.reason)
            }
        }.toolbar {
            Button("Login") {
                viewModel.login()
            }
        }.onReceive(viewModel.error.publisher(LoginViewModel.LoginResultFailure?.self)
                        .replaceError(with: nil)
                        .receive(on: RunLoop.main)
        ) {
            self.error = $0
        }
    }
}

struct Register: View {
    let viewModel: RegisterViewModel
    
    init(viewModel: RegisterViewModel) {
        self.viewModel = viewModel
        self._username = .init(viewModel.username)
        self._password = .init(viewModel.password)
        self._passwordAgain = .init(viewModel.passwordAgain)
        self._firstName = .init(viewModel.firstName)
        self._lastName = .init(viewModel.lastName)
    }
    
    @Binding private var username: String
    @Binding private var password: String
    @Binding private var passwordAgain: String
    @Binding private var firstName: String
    @Binding private var lastName: String
    
    var body: some View {
        Form {
            TextField("Username", text: $username)
            SecureField("Password", text: $password)
            SecureField("Password Again", text: $passwordAgain)
            TextField("First Name", text: $firstName)
            TextField("Last Name", text: $lastName)
        }.toolbar {
            Button("Register") {
                viewModel.register()
            }
        }
    }
}

extension Todo: Swift.Identifiable { }
