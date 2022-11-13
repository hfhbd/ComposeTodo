import SwiftUI
import clients
import Combine

struct ContentView: View {
    init(container: AppContainer) {
        self._container = .init(initialValue: container)
        self.isLoggedIn = container.api.value as! API
    }
    
    @ObservedObject private var container: AppContainer
    @State private var isLoggedIn: API

    var body: some View {
        if isLoggedIn is APILoggedOut {
            TabView {
                NavigationView {
                    Login(loginViewModel: container.loginViewModel)
                        .navigationTitle("Login")
                }.tabItem {
                    Label("Login", systemImage: "person")
                }

                NavigationView {
                    Register(viewModel: container.registerViewModel())
                        .navigationTitle("Register")
                }.tabItem {
                    Label("Register", systemImage: "person.badge.plus")
                }
            }.task {
                for await api in container.api.stream(API.self) {
                    self.isLoggedIn = api
                }
            }
        } else if isLoggedIn is APILoggedIn {
            NavigationView {
                Todos(viewModel: container.todoViewModel())
                    .navigationTitle("Todos")
            }
        }
    }
}
