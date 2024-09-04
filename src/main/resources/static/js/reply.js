// 댓글 등록
async function addReply(data) {
    // 정상적으로 처리되면 서버에서는 "{'id':11}" 과 같은 JSON 데이터를 전송한다
    // 이를 이용해서 댓글이 추가되면 확인창을 보여주고 마지막 페이지로 이동해서 등록된 댓글을 볼 수 있도록 한다
    const response = await axios.post("/replies/", data);
    return response;
}

// 댓글 목록
async function getList({boardId, page, size, goLast}) {
    // bno: 현재 게시물 번호
    // page: 페이지 번호
    // size: 페이지당 사이즈
    // goLast: 마지막 페이지 호출여부
    // 댓글의 경우, 한 페이지에서 모든 동작이 이루어지므로 새로운 댓글이 등록되어도 화면에는 아무런 변화가 없다
    // 또한 페이징 처리가 되면 새로 등록된 댓글이 마지막 페이지에 있기 때문에 댓글된 결과를 볼 수 없다
    // goLast 변수를 이용해서 강제적으로 마지막 댓글 페이지를 호출하도록 한다
    // 댓글 페이징은 새로 댓글이 추가되는 상황이 발생하면 마지막으로 등록되기 때문에 확인이 어렵다
    // 이를 처리하려면 댓글 목록 데이터의 total을 이용해서 다시 마지막 페이지를 호출해야 한다
    // 서버를 다시 호출한다는 단점이 있지만 시시각각으로 댓글의 숫자 등이 변할 수 있기 때문에 이 방법이 무난하다
    // 실제 서비스에서는 댓글 개수를 50, 100개씩 처리해서 다시 호출해야 하는 경우의 수를 줄인다
    // 현재 게시물의 댓글에 마지막 페이지를 알아낸 후, 마지막 페이지를 다시 호출하는 방식으로 동작시킨다
    // 마지막 페이지의 호출은 total 값과 size 값을 이용해서 마지막 페이지를 계산하고 다시 Axios로 호출하는 방식

    const response = await axios.get(`/replies/list/${boardId}`, {params: {page, size}});
    if (goLast) {
        const total = response.data.total;
        const lastPage = parseInt(Math.ceil(total / size));
        return getList({bno: bno, page: lastPage, size: size});
    }
    return response.data;
}
