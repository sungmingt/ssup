import { useErrorStore } from "@/store/errorStore";
import "@/css/GlobalErrorLayer.css";
import { useNavigate } from "react-router-dom";

const GlobalErrorLayer = () => {
  const { error, close } = useErrorStore();
  const navigate = useNavigate();

  if (!error) return null;

  const onClose = () => {
    if (error.redirect === -1) navigate(-1);
    else if (typeof error.redirect === "string") navigate(error.redirect);
    close();
  };

  return (
    <div className="error-overlay">
      <div className="error-modal shadow">
        <h5>{error.title}</h5>
        <p>{error.message}</p>
        <button className="btn confirm-btn" onClick={onClose}>
          확인
        </button>
      </div>
    </div>
  );
};

export default GlobalErrorLayer;
