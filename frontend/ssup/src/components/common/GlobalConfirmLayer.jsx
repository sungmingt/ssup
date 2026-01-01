import { useConfirmStore } from "@/store/confirmStore";
import { useState } from "react";
import "./GlobalConfirmLayer.css";

const GlobalConfirmLayer = () => {
  //confirm이 바뀌면 이 컴포넌트는 자동 리렌더링

  const [submitting, setSubmitting] = useState(false);

  const { confirm, close } = useConfirmStore();
  if (!confirm) return null;

  const handleConfirm = async () => {
    if (submitting) return;

    try {
      setSubmitting(true);
      await confirm.onConfirm(); //호출한 쪽에서 주입한 “확인 시 실행할 로직”을 실행
      close();
    } catch (e) {
      //interceptor가 처리.
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="confirm-overlay">
      <div className="confirm-modal shadow">
        <h5>{confirm.title}</h5>
        <p>{confirm.message}</p>
        <div className="btn-group">
          <button className="btn cancel-btn" onClick={close}>
            취소
          </button>
          <button className="btn confirm-btn" onClick={handleConfirm}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default GlobalConfirmLayer;
