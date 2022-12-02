//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI
import clients

@main
struct ComposeTodoApp: App {
    @StateObject var container = IosContainerKt.IosContainer(storage: UserDefaultStorage())
    
    var body: some Scene {
        WindowGroup {
            ContentView(container: container)
        }
    }
}
