#! /usr/bin/env bash

set -e

case "$OSTYPE" in
  darwin*)
    installer="https://github.com/bazelbuild/bazel/releases/download/0.16.0/bazel-0.16.0-installer-darwin-x86_64.sh"
  ;;
  linux*)
    sudo apt-get install curl pkg-config zip g++ zlib1g-dev unzip python
    installer="https://github.com/bazelbuild/bazel/releases/download/0.16.0/bazel-0.16.0-installer-linux-x86_64.sh"
  ;;
  *)
    echo "Please go to https://github.com/bazelbuild/bazel/releases for bazel installation"
    exit -1
  ;;
esac

curl -s -L $installer | sudo bash
