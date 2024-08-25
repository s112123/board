// 이미지 파일을 서버에 저장
async function uploadToServer(formData) {
    const response = await axios({
        url: '/files/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response;
}

// 서버에서 이미지 파일 삭제
async function removeFileFromServer(uuid, fileName) {
    const response = await axios.delete(`/files/remove/${uuid}_${fileName}`);
    return response;
}