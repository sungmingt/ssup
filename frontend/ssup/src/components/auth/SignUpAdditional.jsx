import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { profileApi } from "@/api";
import { locationApi } from "@/api";
import { interestApi } from "@/api";
import FormLayout from "./../../layouts/FormLayout";
import "./../../css/auth/SignUpAdditional.css";

function SignUpAdditional() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const userId = state?.userId;

  const [siDoId, setSiDoId] = useState("");
  const [siDoList, setSiDoList] = useState([]);
  const [siGunGuList, setSiGunGuList] = useState([]);
  const [selectedSiDo, setSelectedSiDo] = useState("");

  const [interests, setInterests] = useState([]);
  const [selectedInterests, setSelectedInterests] = useState([]);

  const [form, setForm] = useState({
    age: "",
    gender: "MALE",
    intro: "",
    contact: "",
    siGunGuId: "",
  });

  useEffect(() => {
    locationApi.getSiDoList().then((res) => setSiDoList(res.data));
  }, []);

  useEffect(() => {
    interestApi.getAll().then((res) => setInterests(res.data));
  }, []);

  const toggleInterest = (id) => {
    setSelectedInterests((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  const onChangeSiDo = async (e) => {
    const id = e.target.value;
    setSiDoId(id); //DOM 제어
    setSelectedSiDo(id);
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    setSiGunGuList([]);

    if (!id) return;

    const res = await locationApi.getSiGunGuList(id);
    setSiGunGuList(res.data);
  };

  const onChangeSiGunGu = (e) => {
    const value = e.target.value;
    setForm((prev) => ({ ...prev, siGunGuId: value }));
  };

  if (!userId) return <p>잘못된 접근입니다.</p>;

  const onChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    const dto = {
      age: Number(form.age),
      gender: form.gender,
      intro: form.intro,
      contact: form.contact,
      location: {
        siGunGuId: Number(form.siGunGuId),
      },
      interests: selectedInterests.map((id) => ({ interestId: id })),
    };

    const formData = new FormData();
    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], { type: "application/json" })
    );

    try {
      await profileApi.createMyProfile(formData);
      navigate("/profile");
    } catch {
      alert("프로필 저장 실패");
    }
  };

  return (
    <FormLayout>
      <h3 className="fw-bold mb-4 text-center">추가 정보 입력</h3>
      <form onSubmit={onSubmit}>
        <span className="form-label-title">나이</span>

        <input
          className="form-control mb-3"
          name="age"
          placeholder="ex) 30"
          onChange={onChange}
        />

        <span className="form-label-title">성별</span>

        <select name="gender" className="form-select mb-3" onChange={onChange}>
          <option value="MALE">남성</option>
          <option value="FEMALE">여성</option>
        </select>

        <span className="form-label-title">연락처</span>

        <input
          className="form-control mb-3"
          name="contact"
          placeholder="ex) instagram: ssup_insta"
          onChange={onChange}
        />

        <span className="form-label-title">자기소개</span>

        <textarea
          className="form-control mb-3"
          name="intro"
          placeholder="자기소개"
          onChange={onChange}
        />

        <span className="form-label-title">관심사</span>

        <div className="interest-select-box mb-4">
          {interests.map((i) => (
            <span
              key={i.id}
              className={`interest-chip ${
                selectedInterests.includes(i.id) ? "active" : ""
              }`}
              onClick={() => toggleInterest(i.id)}
            >
              {i.name}
            </span>
          ))}
        </div>

        <span className="form-label-title">시/도</span>
        <select
          className="form-select mb-3"
          value={siDoId}
          onChange={onChangeSiDo}
          onKeyDown={(e) => e.key === "Enter" && e.preventDefault()}
        >
          <option value="">시/도 선택</option>
          {siDoList.map((l) => (
            <option key={l.id} value={l.id}>
              {l.name}
            </option>
          ))}
        </select>

        <span className="form-label-title">군/구</span>
        <select
          className="form-select mb-4"
          value={form.siGunGuId}
          onChange={onChangeSiGunGu}
          onKeyDown={(e) => e.key === "Enter" && e.preventDefault()}
          disabled={!selectedSiDo}
        >
          <option value="">군/구 선택</option>
          {siGunGuList.map((l) => (
            <option key={l.id} value={l.id}>
              {l.name}
            </option>
          ))}
        </select>
        <button className="signup-btn btn w-100">완료</button>
      </form>
    </FormLayout>
  );
}

export default SignUpAdditional;
