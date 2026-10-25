
const getErrorMessage = (
  error,
  fallback = "Something went wrong. Please try again."
) => {
  const backendMessage = error?.response?.data?.message;

  if (
    typeof backendMessage === "string" &&
    backendMessage.trim()
  ) {
    return backendMessage;
  }

  const apiMessage = error?.userMessage;

  if (typeof apiMessage === "string" && apiMessage.trim()) {
    return apiMessage;
  }

  return fallback;
};

export default getErrorMessage;
