Run through the whole kata.

Session 1
---------

First we need rough but very slow tests (70')
* Ctrl+Shift+T -> navigate/create new test
* Singleton class is a problem, Grrr. Need all tests in one because I cannot reset the Singleton.
* got 84% mutation coverage, not bad

Now we can make the timer more testable - prepare for faster tests (15')
* extract System.currentTimeMillis with tool, move it to class
* not entirely automated, part in uncovered code was automated.
* Ctrl+Shift+Alt+T -> refactor this

Session 2
---------

Refactor and cleanup first tests (80')
* fix the Singleton problem, split tests.
* can reuse all original tests with stubbed timer.
* IDEA can extract part of strings. Cool.

==> slow tests with good abstractions = 2.5h

Add fast tests (40').
* after extraction of assertions and driver, new tests are easy
* still flaky from time to time 20%

Session 3
---------

Add mock for audio clip (15')
* now the whole audio is not in test scope any more. but it never was.

Add more tests based on Mutation + make tests more rely able (60')
* flaky tests are a problem when assessing mutation coverage
* the more tests I create, the worse it gets.
* got 87% mutation coverage, could still add one or two things

==> fast tests with little more coverage = 2.25h

Analyse BabystepsTimer's Code and Requirements/Domain (60')
* not sure how to start. What is business logic?

> There is a timer.
> * it can be started and stopped and reset.
> * It knows when it started a cycle.
> * It knows how to count down seconds, passed and failed runs,
> * plays sounds and changes colors (states?)
> * calls the UI and receives commands from the UI.
> * Needs callbacks for displayTimer in 3 states (neutral, fail, pass) with current remaining seconds
>
> Sub domains are time, UI/GFX, sounds.
>
> Time:
> * get CurrentTime, difference to earlier time in Seconds = elapsed
> * that would be a value object, returned by the Timer API.
> * is called from core domain
> * also remaining is seconds, but needed +980 millis, too?
> * Seconds is also a value object.
> * Seconds can format as minutes:seconds? -- or is that UI
>
> Sounds:
> * play one of two sounds
> * interface of domain, implementation uses AudioClip (library) abstraction.
> * is called from core domain
>
> Threading/Scheduling:
> * technical, start/stop thread, exchange values
>
> UI:
> * displays Swing timer graphics
> * allows moving around
> * can be on top
> * has colors
> * is called from core domain and also calls into core domain.

Refactor some details (30')
* to separate dependencies I need a IoC mechanism, e.g. construtor.
* maybe first change is to remove all statics. This should simplify tests a bit
  and remove need to access inner states for cleanup
