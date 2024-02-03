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
        HStack {
            if (item.finished) {
                Image(systemName: "checkmark.circle")
            } else {
                Image(systemName: "xmark.circle")
            }
            VStack(alignment: .leading) {
                Text(item.title)
                Text((item.id as! clients.UUID).toNsUUID().uuidString).font(.footnote)
                if let until = item.until {
                    Text(dateFormatter.string(from: until.toNSDate()))
                }
            }
        }
    }
}

private let dateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateStyle = .short
    return formatter
}()

struct TodoItemRowPreview: PreviewProvider {
    static var previews: some View {
        TodoItemRow(item: Todo(id: clients.UUID.companion.NIL, title: "TestItem", until: clients.Instant.companion.fromEpochMilliseconds(epochMilliseconds: 0), finished: false, recordChangeTag: nil))
    }
}
