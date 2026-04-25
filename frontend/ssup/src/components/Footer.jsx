import "@/css/Footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-left">
          <h5 className="footer-logo">ssup!</h5>
          <p className="footer-desc">언어로 연결되는 새로운 친구 관계 🌍</p>
        </div>

        <div className="footer-center">
          <p>📧 ryupl300@gmail.com</p>
          <p>📍 Seoul, South Korea</p>
        </div>

        <div className="footer-right">
          <p>© 2026 ssup. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
