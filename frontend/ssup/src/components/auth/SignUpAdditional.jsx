import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { authApi, locationApi } from "@/api";
import { interestApi } from "@/api";
import { profileApi } from "@/api";
import { languageApi } from "@/api";
import { useAuthStore } from "@/store/authStore";
import FormLayout from "./../../layouts/FormLayout";
import defaultProfile from "@/assets/ssup_user_default_image.png";
import "./../../css/auth/SignUpAdditional.css";

function SignUpAdditional() {
  const navigate = useNavigate();

  const { user, isAuthenticated, loading } = useAuthStore();
  const [pageLoading, setPageLoading] = useState(false);

  const [image, setImage] = useState(null);
  const [preview, setPreview] = useState(null);

  const [siDoId, setSiDoId] = useState("");
  const [siDoList, setSiDoList] = useState([]);
  const [siGunGuList, setSiGunGuList] = useState([]);
  const [selectedSiDo, setSelectedSiDo] = useState("");

  const [interests, setInterests] = useState([]);
  const [selectedInterests, setSelectedInterests] = useState([]);

  const [languages, setLanguages] = useState({ using: [], learning: [] });
  const [allLanguages, setAllLanguages] = useState([]);
  const [langModalType, setLangModalType] = useState(null);

  const [submitting, setSubmitting] = useState(false);

  const [form, setForm] = useState({
    age: "",
    gender: "MALE",
    intro: "",
    contact: "",
    siGunGuId: "",
  });

  useEffect(() => {
    if (loading) return;

    if (!isAuthenticated || !user) {
      navigate("/login", { replace: true });
      return;
    }

    if (user.status !== "PENDING") {
      navigate("/", { replace: true });
    }
  }, [loading, isAuthenticated, user, navigate]);

  // 초기 로드 (언어 목록 추가)
  useEffect(() => {
    const load = async () => {
      try {
        setPageLoading(true);
        const [siDoRes, interestRes, langRes] = await Promise.all([
          locationApi.getSiDoList(),
          interestApi.getAll(),
          languageApi.getLanguageList(), // 언어 목록 추가
        ]);
        setSiDoList(siDoRes.data);
        setInterests(interestRes.data);
        setAllLanguages(langRes.data);
      } catch (e) {
        console.error(e);
      } finally {
        setPageLoading(false);
      }
    };
    load();
  }, []);

  useEffect(() => {
    if (pageLoading) return;

    const load = async () => {
      try {
        setPageLoading(true);

        const [siDoRes, interestRes] = await Promise.all([
          locationApi.getSiDoList(),
          interestApi.getAll(),
        ]);

        setSiDoList(siDoRes.data);
        setInterests(interestRes.data);
      } catch (e) {
        console.error(e);
      } finally {
        setPageLoading(false);
      }
    };

    load();
  }, []);

  const toggleLanguage = (lang) => {
    const key = langModalType === "USING" ? "using" : "learning";
    setLanguages((prev) => {
      const exists = prev[key].some((l) => l.id === lang.id);
      return {
        ...prev,
        [key]: exists
          ? prev[key].filter((l) => l.id !== lang.id)
          : [...prev[key], { ...lang, level: "BEGINNER" }], // 초기 레벨 BEGINNER
      };
    });
  };

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

  const onImageChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setImage(file);
    setPreview(URL.createObjectURL(file));
  };

  const removeProfileImage = () => {
    setImage(null);
    setPreview(null);
  };

  const onChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    if (submitting) return;
    setSubmitting(true);

    e.preventDefault();

    const dto = {
      age: Number(form.age),
      gender: form.gender,
      intro: form.intro,
      contact: form.contact,
      location: { siGunGuId: Number(form.siGunGuId) },
      interests: selectedInterests.map((id) => ({ interestId: id })),
      languages: [
        ...languages.using.map((l) => ({
          languageId: l.id,
          type: "USING",
          level: l.level,
        })),
        ...languages.learning.map((l) => ({
          languageId: l.id,
          type: "LEARNING",
          level: l.level,
        })),
      ],
    };
    const formData = new FormData();
    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], { type: "application/json" })
    );

    if (image instanceof File) formData.append("image", image);

    try {
      await profileApi.createMyProfile(formData);
      await languageApi.updateMyLanguages({
        languages: [
          ...languages.using.map((l) => ({
            languageId: l.id,
            type: "USING",
            level: l.level,
          })),
          ...languages.learning.map((l) => ({
            languageId: l.id,
            type: "LEARNING",
            level: l.level,
          })),
        ],
      });

      await useAuthStore.getState().userInit();
      navigate("/profile");
    } catch {
      alert("프로필 저장 실패");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading || pageLoading) return null;

  return (
    <FormLayout>
      <h3 className="fw-bold mb-4 text-center">추가 정보 입력</h3>
      <form onSubmit={onSubmit}>
        {/* === 프로필 이미지 === */}
        <div className="text-center mb-4">
          <img
            src={preview || defaultProfile}
            className="profile-edit-avatar mb-2"
          />

          <div className="interest-select-box justify-content-center">
            <label className="interest-chip">
              이미지 변경
              <input type="file" hidden onChange={onImageChange} />
            </label>

            {preview && (
              <span className="interest-chip" onClick={removeProfileImage}>
                이미지 삭제
              </span>
            )}
          </div>
        </div>

        {/* === 언어 선택 추가 (ProfileEdit 방식) === */}
        <span className="form-label-title">언어</span>
        <button
          type="button"
          className="form-control text-start mb-2"
          onClick={() => setLangModalType("USING")}
        >
          {languages.using.length
            ? languages.using.map((l) => l.name).join(", ")
            : "사용 언어 선택"}
        </button>
        <button
          type="button"
          className="form-control text-start mb-3"
          onClick={() => setLangModalType("LEARNING")}
        >
          {languages.learning.length
            ? languages.learning.map((l) => l.name).join(", ")
            : "학습 언어 선택"}
        </button>

        {/* 언어 선택 모달 */}
        {langModalType && (
          <div className="language-modal-overlay">
            <div className="language-modal">
              <h5 className="mb-3">
                {langModalType === "USING" ? "사용 언어" : "학습 언어"} 선택
              </h5>
              <div className="interest-select-box mb-4">
                {allLanguages.map((l) => {
                  const selected =
                    langModalType === "USING"
                      ? languages.using.some((x) => x.id === l.id)
                      : languages.learning.some((x) => x.id === l.id);
                  return (
                    <span
                      key={l.id}
                      className={`interest-chip ${selected ? "active" : ""}`}
                      onClick={() => toggleLanguage(l)}
                    >
                      {l.name}
                    </span>
                  );
                })}
              </div>
              <div className="text-center">
                <button
                  type="button"
                  className="signup-btn px-4"
                  onClick={() => setLangModalType(null)}
                >
                  완료
                </button>
              </div>
            </div>
          </div>
        )}

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
