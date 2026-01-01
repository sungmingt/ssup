import "./FormLayout.css";

export default function FormLayout({ children }) {
  return (
    <div className="form-wrapper">
      <div className="form-card">{children}</div>
    </div>
  );
}
