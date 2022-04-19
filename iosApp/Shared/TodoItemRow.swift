//
//  TodoItemRow.swift.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 04.11.21.
//

import SwiftUI
import clients

struct TodoItemRow: View {
    let item: Todo

    var body: some View {
        VStack {
            Text(item.title)
        }
    }
}

struct TodoItemRowPreview: PreviewProvider {
    static var previews: some View {
        TodoItemRow(item: Todo(id: UUID.companion.NIL, title: "TestItem", until: nil, finished: false, recordChangeTag: nil))
    }
}
