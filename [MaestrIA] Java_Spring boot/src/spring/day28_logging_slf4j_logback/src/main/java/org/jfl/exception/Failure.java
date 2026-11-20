package org.jfl.exception;

public sealed interface Failure permits NotFound, Invalid, Timeout{
}
