// 댓글 등록
async function addReply(data) {
    // 정상적으로 처리되면 서버에서는 "{'id':11}" 과 같은 JSON 데이터를 전송한다
    // 이를 이용해서 댓글이 추가되면 확인창을 보여주고 마지막 페이지로 이동해서 등록된 댓글을 볼 수 있도록 한다
    const response = await axios.post("/replies/", data);
    return response;
}