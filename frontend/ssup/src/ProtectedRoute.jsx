import { useAuthStore } from "@/store/authStore";
import { Navigate } from "react-router-dom";
import Spinner from "@/Spinner";

//권한 제어 및 리다이렉트 주체
const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuthStore();

  if (loading) return <Spinner />;

  if (!user) return <Navigate to="/login" replace />;

  if (user.status === "PENDING") {
    return <Navigate to="/signup/additional" replace />;
  }

  return children;
};

export default ProtectedRoute;
