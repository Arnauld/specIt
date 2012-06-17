
* Executable documentation
* Clears out the misunderstandings between Customers, Domain Experts, Developers...


# Features

Actuals:

* Pure Java implementation
* Text-based user stories
* Annotation-based binding of textuel steps to corresponding Java methods
* Highly customizable: keywords bindings, i18n, ...
* Test frameworks agnostic (JUnit, TestNG, FestAssert, ...)
* Tagging and metadata

Incomings:

* Story reporting (Console, Html, ...)
* DI support (Spring, Guice)
* Jenkins plugin
* IDE integration (syntax coloring, completion, ...)

# Code organization

    [*.story] -> /Parser/ -> [RawPart]* -> /StoryBuilder/ -> [Story] -> /Interpreter/ -> result

