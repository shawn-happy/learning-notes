`The command pattern encapsulates a request as an object, thereby letting us parameterize other objects with different requests, queue or log requests, and support undoable operations.`

命令模式将请求封装为一个对象，这样可以使用不同的请求参数化其他对象（将不同请求依赖注入到其他对象），并且能够支持请求（命令）的排队执行，记录日志，撤销等（附加控制）功能。

命令模式的主要作用和应用场景：用来控制命令的执行，比如，异步，延迟，排队执行命令，撤销重做命令，存储命令，记录日志等等。

