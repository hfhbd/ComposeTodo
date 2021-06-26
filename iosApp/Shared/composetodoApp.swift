//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI
import shared

@main
struct ComposeTodoApp: App {
    private let persistenceController = PersistenceController()
    
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: ViewModel(api: ClientKt.api(cookiesStorage: RefreshTokenStorage())))
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
