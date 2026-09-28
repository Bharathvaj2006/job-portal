
function Dashboard() {
  const handleLogout = () => {
    sessionStorage.removeItem("token");
    window.location.href = "/login";
  };

  return (
    <div style={{ padding: "40px", fontFamily: "Arial, sans-serif" }}>
      <h1>Candidate Dashboard</h1>
      <p>Welcome to your Job Portal!</p>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default Dashboard;