# AndroidHacks
Some experiments on android

## Gems

* See `ActivityUseCase.delayByAlarm` for how to make a coroutine continue by wrapping the
continuation inside a Messenger, then wrap that Messenger to a PendingIntent (which will start a
Service e.g), then make that Service to invoke the `Continuation.resume` when fired.

