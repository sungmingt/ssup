import { useState, useEffect } from "react";
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./components/LoginForm";
import PostList from "./components/postList/PostList";
import Login from "./components/Login";
import Profile from "./components/user/Profile";
import ProfileEdit from "./components/user/ProfileEdit";
import PostCreateForm from "./components/post/PostCreateForm";
import PostUpdateForm from "./components/post/PostUpdateForm";
import Post from "./components/post/Post";
import Header from "./components/Header";
import SignUp from "./components/auth/SignUp";
import SignUpAdditional from "./components/auth/SignUpAdditional";
import MainPage from "./components/MainPage";
import "./App.css";
import { useAuthStore } from "./store/authStore";
import ProtectedRoute from "@/routes/ProtectedRoute";
import GlobalErrorLayer from "@/components/common/GlobalErrorLayer";
import GlobalConfirmLayer from "@/components/common/GlobalConfirmLayer";
import MatchHistory from "./components/match/MatchHistory";

function App() {
  const initAuth = useAuthStore((s) => s.initAuth);
  const loading = useAuthStore((s) => s.loading);

  useEffect(() => {
    initAuth();
  }, []);

  if (loading) return null;

  return (
    <Router>
      <div className="app-wrapper">
        <Header />
        <GlobalConfirmLayer />
        <GlobalErrorLayer />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/posts/:id" element={<Post />} />
          <Route path="/posts" element={<PostList />} />
          <Route
            path="/posts/create"
            element={
              <ProtectedRoute>
                <PostCreateForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/posts/:id/update"
            element={
              <ProtectedRoute>
                <PostUpdateForm />
              </ProtectedRoute>
            }
          />

          <Route path="/profile" element={<Profile isMyProfile />} />
          <Route path="/users/:id/profile" element={<Profile />} />
          <Route
            path="/me/profile/edit"
            element={
              <ProtectedRoute>
                <ProfileEdit />
              </ProtectedRoute>
            }
          />

          <Route path="/me/matches" element={<MatchHistory />} />

          <Route path="/signup" element={<SignUp />} />
          <Route path="/signup/additional" element={<SignUpAdditional />} />

          <Route path="/login" element={<Login />} />
        </Routes>
        {/* <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/posts" element={<PostList />} />
          <Route path="/posts/create" element={<PostCreateForm />} />
          <Route path="/posts/:id" element={<Post />} />
        </Routes> */}
      </div>
    </Router>
  );
}
export default App;
