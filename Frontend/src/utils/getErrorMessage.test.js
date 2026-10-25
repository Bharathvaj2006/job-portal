
import { describe, expect, it } from "vitest";
import getErrorMessage from "./getErrorMessage";

describe("getErrorMessage", () => {
  it("returns the backend error message when available", () => {
    const error = {
      response: {
        data: {
          message: "Resume upload failed",
        },
      },
      userMessage: "A network error occurred",
    };

    expect(getErrorMessage(error)).toBe("Resume upload failed");
  });

  it("returns the Axios userMessage when no backend message exists", () => {
    const error = {
      userMessage: "Unable to connect to the server.",
    };

    expect(getErrorMessage(error)).toBe(
      "Unable to connect to the server."
    );
  });

  it("ignores an empty backend message", () => {
    const error = {
      response: {
        data: {
          message: "   ",
        },
      },
      userMessage: "A server error occurred.",
    };

    expect(getErrorMessage(error)).toBe(
      "A server error occurred."
    );
  });

  it("returns the supplied fallback when no message exists", () => {
    expect(
      getErrorMessage({}, "Unable to load resume.")
    ).toBe("Unable to load resume.");
  });

  it("returns the default fallback for an empty error", () => {
    expect(getErrorMessage(null)).toBe(
      "Something went wrong. Please try again."
    );
  });
});
