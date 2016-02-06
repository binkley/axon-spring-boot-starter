# How to Contribute

## Getting Started

* Make sure you have a [GitHub account](https://github.com/signup/free).
* Submit a ticket for your issue, assuming one does not already exist.
  * Clearly describe the issue including steps to reproduce when it is a bug.
  * Make sure you fill in the earliest version that you know has the issue.
* Fork the repository on GitHub.
* Add yourself to the `<contributors/>` block in `pom.xml` with the role,
  "contributor".

## Making Changes

* Create a feature branch from where you want to base your work.
  * This is usually the master branch.
  * Only target release branches if you are certain your fix must be on that
    branch.
  * To quickly create a feature branch based on master; `git checkout -b
    fix/master/my_contribution master`. Please avoid working directly on the
    `master` branch.
  * Do not push feature branches.  If you need to share code, send patches to
    the collaborators.
* Make commits of logical units.  Squash if needed.

## Code Style

* Use standard Java code style.  You know it when you see it.  Exceptions:
  * For empty classes and methods, it is OK to collapse the curly braces
    rather than put them on separate lines.
  * For block syntax, it is OK to leave out optional curly braces if the
    meaning of the code does not change.
* Ensure all changes use UNIX file endings and UTF-8 encoding, and that only
  scripts have an execute bit (source files are not executable).
* Check for unnecessary whitespace with `git diff --check` before committing.
