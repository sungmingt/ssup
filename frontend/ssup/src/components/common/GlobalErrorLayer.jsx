import { useErrorStore } from "@/store/errorStore";
import "./GlobalErrorLayer.css";
import { useNavigate } from "react-router-dom";

//GlobalErrorLayer는 useErrorStore()를 통해 error 상태를 구독하고 있음.
//상태변화 -> react가 자동으로 리렌더링
//if(!error) 을 통과하지 못하고 모달을 렌더링
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
