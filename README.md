This branch contains the GitHub workflows being used in the JePL repository:

- `jenkins.yaml` manages the creation of the `jenkins/*` branches for those
matching the prefixes:
  - `release/*`
  - `feature/*`
  - `fix/*`
- `stable-milestone.yaml` manages the creation of the `stable/*` branch when
the corresponding milestone (identified by the release version) is closed.
  - Used in the `master` branch, as milestone events are only processed from
  the default branch.
