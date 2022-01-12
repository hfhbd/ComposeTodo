//
//  composetodoApp.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI
import shared
import Combine

@main
struct ComposeTodoApp: App {
    let container = IosContainer()
    
    var body: some Scene {
        WindowGroup {
            ContentView(container: container)
        }
    }
}
