import { useState, useEffect, useRef, useCallback } from "react";
import PostCard from "./PostCard.jsx";
import PostTopBar from "./PostTopBar.jsx";
import SearchModal from "./SearchModal.jsx";
import { postApi } from "@/api";

const PAGE_SIZE = 9;

const PostList = () => {
  //state
  const [posts, setPosts] = useState([]);
  const [sortType, setSortType] = useState("LATEST");
  const [loading, setLoading] = useState(false);

  const [filters, setFilters] = useState({
    locationId: null,
    usingLanguage: null,
    learningLanguage: null,
    interestId: null,
  });

  //ref
  const pageRef = useRef({
    cursorKey: null,
    cursorId: null,
    hasNext: true,
    loading: false,
  });

  const isReadyRef = useRef(false); // 초기 1페이지 로딩 완료 여부
  const observerRef = useRef(null);

  //data loading
  const loadPosts = useCallback(
    async (isFirst = false) => {
      const page = pageRef.current;

      // 🔒 중복 / 불필요 호출 차단
      if (page.loading) return;
      if (!page.hasNext && !isFirst) return;

      page.loading = true;
      setLoading(true);

      try {
        const res = await postApi.getPostList({
          sortType,
          cursorKey: isFirst ? null : page.cursorKey,
          cursorId: isFirst ? null : page.cursorId,
          size: PAGE_SIZE,
        });

        const data = res.data;

        setPosts((prev) => {
          const existingIds = new Set(prev.map((p) => p.id));
          const newItems = data.items.filter(
            (item) => !existingIds.has(item.id)
          );
          return isFirst ? newItems : [...prev, ...newItems];
        });

        // 🔄 커서 갱신
        page.cursorKey = data.nextCursorKey;
        page.cursorId = data.nextCursorId;
        page.hasNext = data.hasNext;

        // ✅ 첫 페이지 로딩 완료 신호
        if (isFirst) {
          isReadyRef.current = true;
        }
      } catch (e) {
        console.error("게시글 불러오기 실패", e);
      } finally {
        page.loading = false;
        setLoading(false);
      }
    },
    [sortType]
  );

  /** =========================
   *  최초 진입 / 정렬 변경
   *  ========================= */
  useEffect(() => {
    setPosts([]);
    isReadyRef.current = false;

    pageRef.current = {
      cursorKey: null,
      cursorId: null,
      hasNext: true,
      loading: false,
    };

    loadPosts(true);
  }, [sortType, loadPosts]);

  /** =========================
   *  IntersectionObserver
   *  (한 번만 생성)
   *  ========================= */
  useEffect(() => {
    if (!observerRef.current) return;
    if (!isReadyRef.current) return;

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (!entry.isIntersecting) return;
        // if (!isReadyRef.current) return; // 🔥 초기 로딩 중 차단

        loadPosts();
      },
      {
        root: null,
        rootMargin: "200px",
        threshold: 0,
      }
    );

    observer.observe(observerRef.current);
    return () => observer.disconnect();
  }, [loadPosts, posts.length]);

  //redner
  return (
    <>
      <div className="container py-5">
        <PostTopBar
          sortType={sortType}
          setSortType={setSortType}
          filters={filters}
          setFilters={setFilters}
        />

        <div className="row">
          {posts.map((post) => (
            <PostCard key={post.id} post={post} />
          ))}
        </div>

        {loading && (
          <p className="text-center mt-3 text-muted">게시글 불러오는 중...</p>
        )}

        {!loading && posts.length === 0 && (
          <p className="text-center mt-3">게시글이 없습니다.</p>
        )}

        <SearchModal />
      </div>

      {/* 🔥 반드시 container 밖 */}
      <div ref={observerRef} style={{ height: "200px" }} />
    </>
  );
};

export default PostList;
