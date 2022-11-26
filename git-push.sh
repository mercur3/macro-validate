#!/bin/sh

set -ex
cd $(dirname $0)

git push origin --all
git push origin --tags
git push https://gitlab.com/mercur3/macro-validate.git --all
git push https://gitlab.com/mercur3/macro-validate.git --tags

