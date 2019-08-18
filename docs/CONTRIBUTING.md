First off, thanks for taking the time to contribute! 👏
- [Found an Issue or Bug?](fib)
- [Missing a Feature?](maf)
- [Want a fix doc?](doc)
- [Pull Request Submission Guidelines](pr)
- [Release](Release)

## <a name="fib"></a> Found an Issue or Bug?
If you find a bug in the source code, you can help json-values by submitting an [issue](https://github.com/imrafaelmerino/json-values/issues/new?assignees=imrafaelmerino&labels=bug&template=bug_report.md&title=) to the GitHub Repository. Even better, you can submit a Pull Request with a fix.
## <a name="maf"></a> Missing a Feature?
You can request a new feature by submitting an [issue](https://github.com/imrafaelmerino/json-values/issues/new?assignees=imrafaelmerino&labels=enhancement&template=feature_request.md&title=) 
to the GitHub Repository.
If you would like to implement a new feature, then consider what kind of change it is:

Significant Changes that you wish to contribute to the project should be discussed first in a GitHub issue that clearly outlines the changes and benefits of the feature.

Small Changes can directly be crafted and submitted to the GitHub Repository as a Pull Request. 
## <a name="doc"></a> Want a fix doc?
If you find a typo, a better way of explaining something, or you want to add something new, please,
open an issue and submit a pull request with the update. I'm not a native english speaker, so all your feedback
is more than welcoming.
## <a name="pr"></a> Pull Request Submission Guidelines
Commit your changes using a descriptive commit message that follows the [AngularJS commit conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153). 
Adherence to the commit message conventions is required because release notes are automatically generated from these messages.
I use [git-cz](https://www.npmjs.com/package/git-cz) for this purpose and [git-changelog](https://github.com/git-chglog/git-chglog) to generate the changelog file of
every release. You'll be able to confirm the pull request if all the checks pass. For now the project is integrated with TravisCI, CircleCI, and SonarCloud.

## <a name="release"></a> Release
After merging the pull request, I tag master with the new version number, which triggers the
release process in TravisCI. It deploys the artifact to Maven Central and creates a draft of the release in GitHub. After checking that the artifact
can be found in [Maven Central](https://search.maven.org/search?q=g:com.github.imrafaelmerino%20a:json-values), which usually takes from ten minutes to 10 hours, 
and after the Javadoc has been uploaded to [javadoc.io](https://www.javadoc.io/doc/com.github.imrafaelmerino/json-values), which takes up 
to 24 hours, I consolidate the version in GitHub. 
