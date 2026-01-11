import { useState, useEffect, useCallback, useMemo } from "react";
import { postApi } from "@/api";

export const usePostFilterStore = () => {
  const [filterData, setFilterData] = useState({
    locations: [],
    languages: [],
    interests: [],
    isLoaded: false,
  });

  const fetchFilterMetadata = useCallback(async () => {
    if (filterData.isLoaded) return;

    const cached = localStorage.getItem("post_filter_metadata");

    if (cached) {
      const cachedData = JSON.parse(cached);
      Promise.resolve().then(() => {
        setFilterData({ ...cachedData, isLoaded: true });
      });

      return;
    }

    try {
      const res = await postApi.getFilterMedadata();
      const data = res.data;

      //리액트 렌더링 중 데이터 로드를 방지하기 위해
      Promise.resolve().then(() => {
        setFilterData({
          locations: data.locations || [],
          languages: data.languages || [],
          interests: data.interests || [],
          isLoaded: true,
        });
      });

      localStorage.setItem("post_filter_metadata", JSON.stringify(data));
    } catch (e) {
      console.error("게시글 필터 정보 로딩 실패:", e);
    }
  }, [filterData.isLoaded]);

  //초기 로딩을 위해
  useEffect(() => {
    fetchFilterMetadata();
  }, [fetchFilterMetadata]);

  return {
    locations: filterData.locations,
    languages: filterData.languages,
    interests: filterData.interests,
    isLoaded: filterData.isLoaded,
    fetchFilterMetadata,
  };
};
