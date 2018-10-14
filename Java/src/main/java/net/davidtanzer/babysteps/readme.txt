Run through the whole kata.

Session 1
---------

First we need rough but very slow tests (70')
* Ctrl+Shift+T -> navigate/create new test
* Singleton class is a problem, Grrr. Need all tests in one because I cannot reset the Singleton.

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

Add fast tests (40').
* after extraction of assertions and driver, new tests are easy
* still flaky from time to time 20%

Session 3
---------

Add mock for audio clip (15')
* now the whole audio is not in test scope any more. but it never was.

Add more tests based on Mutation + make tests more rely able (1h)
* flaky tests are a problem when assessing mutation coverage
* the more tests I create, the worse it gets.
