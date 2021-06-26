//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI

@main
struct ComposeTodoApp: App {
    private let persistenceController = PersistenceController()

    @StateObject private var model = ViewModel()

    var body: some Scene {
        WindowGroup {
            NavigationView {
                ContentView(viewModel: model)
                    .environment(\.managedObjectContext, persistenceController.container.viewContext)
            }
        }
    }
}
