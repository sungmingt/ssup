import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./components/LoginForm";
import PostList from "./components/PostList";
import Login from "./components/Login";
import Profile from "./components/Profile";
import PostCreateForm from "./components/post/PostCreateForm";
import Post from "./components/post/Post";
import Header from "./components/Header";
import SignUp from "./components/auth/SignUp";
import MainPage from "./components/MainPage.jsx";
import "./App.css";

function App() {
  return (
    <Router>
      <div className="app-wrapper">
        <Header />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/posts" element={<PostList />} />
          <Route path="/posts/create" element={<PostCreateForm />} />
          <Route path="/posts/:id" element={<Post />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
