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
    @StateObject var container = IosContainer(storage: UserDefaultStorage())
    
    var body: some Scene {
        WindowGroup {
            ContentView(container: container)
        }
    }
}

actor UserDefaultStorage: NSObject, Ktor_client_coreCookiesStorage {
    @AppStorage("refreshToken")
    private var token: String?
    
    func addCookie(requestUrl: Ktor_httpUrl, cookie: Ktor_httpCookie) async throws {
        token = cookie.value
    }
    
    func get(requestUrl: Ktor_httpUrl) async throws -> [Ktor_httpCookie]? {
        if let token {
            return [Ktor_httpCookie(name: "SESSION", value: token, encoding: Ktor_httpCookieEncoding.uriEncoding, maxAge: 0, expires: nil, domain: nil, path: nil, secure: true, httpOnly: true, extensions: [:])]
        } else {
            return []
        }
    }
    
    nonisolated func close() {
        
    }
}
