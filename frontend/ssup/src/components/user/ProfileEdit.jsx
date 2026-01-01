import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { profileApi, interestApi, locationApi, languageApi } from "@/api";
import FormLayout from "@/layouts/FormLayout";
import defaultProfile from "@/assets/ssup_user_default_image.png";
import "./../../css/user/ProfileEdit.css";

function ProfileEdit() {
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [image, setImage] = useState(null);
  const [preview, setPreview] = useState(null);

  const [form, setForm] = useState({
    nickname: "",
    age: "",
    gender: "MALE",
    contact: "",
    intro: "",
    siGunGuId: "",
    interestIds: [],
    removeImage: false,
  });

  const [interests, setInterests] = useState([]);
  const [siDoList, setSiDoList] = useState([]);
  const [siGunGuList, setSiGunGuList] = useState([]);
  const [siDoId, setSiDoId] = useState("");
  const [selectedSiDo, setSelectedSiDo] = useState(false);

  const [languages, setLanguages] = useState({ using: [], learning: [] });
  const [allLanguages, setAllLanguages] = useState([]);
  const [langModalType, setLangModalType] = useState(null);

  const modalRoot = document.getElementById("modal-root");

  const LEVELS = [
    "NATIVE",
    "ADVANCED",
    "INTERMEDIATE",
    "ELEMENTARY",
    "BEGINNER",
  ];

  useEffect(() => {
    profileApi.getMyProfile().then((res) => {
      const p = res.data;
      setProfile(p);
      setForm({
        nickname: p.nickname,
        age: p.age || "",
        gender: p.gender || "MALE",
        contact: p.contact || "",
        intro: p.intro || "",
        siGunGuId: p.location.siGunGuId,
        interestIds: p.interests.map((i) => i.id),
        removeImage: false,
      });
      setPreview(p.imageUrl || defaultProfile);
    });

    interestApi.getAll().then((res) => setInterests(res.data));
    locationApi.getSiDoList().then((res) => setSiDoList(res.data));
    languageApi.getLanguageList().then((res) => setAllLanguages(res.data));
  }, []);

  useEffect(() => {
    if (!profile) return;

    languageApi.getUserLanguages(profile.id).then((res) => {
      setLanguages({
        using: res.data.usingLanguages,
        learning: res.data.learningLanguages,
      });
    });
  }, [profile]);

  const openLangModal = (type) => {
    setLangModalType(type);
  };

  const closeLangModal = () => setLangModalType(null);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onImageChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setImage(file);
    setPreview(URL.createObjectURL(file));
    setForm((p) => ({ ...p, removeImage: false }));
  };

  const removeProfileImage = () => {
    setImage(null);
    setPreview(defaultProfile);
    setForm((p) => ({ ...p, removeImage: true }));
  };

  const toggleInterest = (id) =>
    setForm((p) => ({
      ...p,
      interestIds: p.interestIds.includes(id)
        ? p.interestIds.filter((x) => x !== id)
        : [...p.interestIds, id],
    }));

  const onChangeSiDo = async (e) => {
    const value = e.target.value;
    setSiDoId(value);
    setSelectedSiDo(!!value);
    if (!value) return;
    const res = await locationApi.getSiGunGuList(value);
    setSiGunGuList(res.data);
  };

  const toggleLanguage = (lang) => {
    const key = langModalType === "USING" ? "using" : "learning";

    setLanguages((prev) => {
      const exists = prev[key].some((l) => l.id === lang.id);

      return {
        ...prev,
        [key]: exists
          ? prev[key].filter((l) => l.id !== lang.id)
          : [...prev[key], { ...lang, level: lang.level ?? "BEGINNER" }],
      };
    });
  };

  const changeLevel = (type, id, level) =>
    setLanguages((p) => ({
      ...p,
      [type === "USING" ? "using" : "learning"]: p[
        type === "USING" ? "using" : "learning"
      ].map((l) => (l.languageId === id ? { ...l, level } : l)),
    }));

  const onSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append(
      "dto",
      new Blob(
        [
          JSON.stringify({
            ...form,
            userLocationUpdateRequest: { siGunGuId: form.siGunGuId },
          }),
        ],
        { type: "application/json" }
      )
    );

    if (image instanceof File) formData.append("image", image);

    await profileApi.updateMyProfile(formData);

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

    navigate("/profile", { state: { userId: profile.id } });
  };

  if (!profile) return null;

  return (
    <FormLayout>
      <h3 className="fw-bold mb-4 text-center">프로필 수정</h3>

      <form onSubmit={onSubmit}>
        {/* === 이미지 === */}
        <div className="text-center mb-4">
          <img
            src={preview}
            style={{
              width: 96,
              height: 96,
              borderRadius: "50%",
              objectFit: "cover",
            }}
          />
          <div className="interest-select-box justify-content-center mt-2">
            <label className="interest-chip">
              이미지 변경
              <input type="file" hidden onChange={onImageChange} />
            </label>
            <span className="interest-chip" onClick={removeProfileImage}>
              이미지 삭제
            </span>
          </div>
        </div>

        {/* === 언어 === */}
        <span className="form-label-title">언어</span>

        <button
          type="button"
          className="form-control text-start mb-2"
          onClick={(e) => {
            console.log("USING CLICK");
            e.stopPropagation();
            e.stopPropagation();
            setLangModalType("USING");
          }}
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

        {langModalType && (
          <div className="language-modal-overlay">
            <div className="language-modal">
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

        {/* === 기본 필드 === */}
        <span className="form-label-title">닉네임</span>
        <input
          className="form-control mb-3"
          name="nickname"
          value={form.nickname}
          onChange={onChange}
        />

        <span className="form-label-title">나이</span>
        <input
          className="form-control mb-3"
          name="age"
          value={form.age}
          onChange={onChange}
        />

        <span className="form-label-title">성별</span>
        <select
          name="gender"
          className="form-select mb-3"
          value={form.gender}
          onChange={onChange}
        >
          <option value="MALE">남성</option>
          <option value="FEMALE">여성</option>
        </select>

        <span className="form-label-title">연락처</span>
        <input
          className="form-control mb-3"
          name="contact"
          value={form.contact}
          onChange={onChange}
        />

        <span className="form-label-title">자기소개</span>
        <textarea
          className="form-control mb-3"
          name="intro"
          value={form.intro}
          onChange={onChange}
        />

        <span className="form-label-title">관심사</span>
        <div className="interest-select-box mb-4">
          {interests.map((i) => (
            <span
              key={i.id}
              className={`interest-chip ${
                form.interestIds.includes(i.id) ? "active" : ""
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
          name="siGunGuId"
          value={form.siGunGuId}
          onChange={onChange}
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

export default ProfileEdit;
