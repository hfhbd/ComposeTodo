//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI

@main
struct ComposeTodoApp: App {
    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            NavigationView {
                ContentView().environment(\.managedObjectContext, persistenceController.container.viewContext)
            }
        }
    }
}
