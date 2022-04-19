import clients

struct FlowStream<T>: AsyncSequence {
    func makeAsyncIterator() -> FlowAsyncIterator {
        FlowAsyncIterator(iterator: FlowsKt.asAsyncIterable(flow, context: context))
    }

    typealias AsyncIterator = FlowAsyncIterator

    typealias Element = T

    private let flow: Flow
    private let context: KotlinCoroutineContext

    init (_ type: T.Type, flow: Flow, context: KotlinCoroutineContext) {
        self.flow = flow
        self.context = context
    }

    struct FlowAsyncIterator: AsyncIteratorProtocol {
        let iterator: IteratorAsync

        @MainActor
        func next() async -> T? {
            return try! await withTaskCancellationHandler(handler: {
                iterator.cancel()
            }) {
                do {
                    return Optional.some(try await iterator.next() as! T)
                } catch let error as NSError {
                    let kotlinException = error.kotlinException
                    if kotlinException is KotlinCancellationException {
                        return nil
                    } else {
                        throw error
                    }
                }
            }
        }

        typealias Element = T
    }
}

extension Flow {
    func stream<T>(_ t: T.Type, context: KotlinCoroutineContext = Dispatchers.shared.Default) -> FlowStream<T> {
        FlowStream(t, flow: self, context: context)
    }
}
