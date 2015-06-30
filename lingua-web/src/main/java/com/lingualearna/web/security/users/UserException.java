package com.lingualearna.web.security.users;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicates a particular problem with a user after creation/modification
 */
public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1313101137284164932L;

    private List<String> problems;

    public UserException(String... problemsArg) {

        problems = new ArrayList<>();

        for (String problem : problemsArg) {
            problems.add(problem);
        }
    }

    public List<String> getProblems() {

        return problems;
    }

    public void setProblems(List<String> problems) {

        this.problems = problems;
    }
}
