function toggleEditForm(btn) {
    const commentBox = btn.closest('.comment-item');
    const form = commentBox.querySelector('.comment-edit-form');
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
}

function cancelEditForm(btn) {
    const form = btn.closest('.comment-edit-form');
    form.style.display = 'none';
}

function setReplyTarget(parentId, nickname, content) {
    document.getElementById("parentId").value = parentId;
    document.getElementById("grandParentId").value = "";
    const replyRef = document.getElementById("replyReference");
    replyRef.style.display = "block";
    replyRef.innerText = `↪️ @${nickname} "${content}"`;
}

function deleteComment(commentId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    fetch(`/board/deleteComment?id=${commentId}`, { method: "POST" })
        .then(() => location.reload());
}
